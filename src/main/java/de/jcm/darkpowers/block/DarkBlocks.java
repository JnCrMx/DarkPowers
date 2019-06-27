package de.jcm.darkpowers.block;

import de.jcm.darkpowers.DarkPowers;
import de.jcm.darkpowers.block.BlockRune.RuneType;

import cpw.mods.fml.common.registry.GameRegistry;

public class DarkBlocks
{
	// Blocks
	public static BlockDarkness blockDarkness;
	public static BlockAltar blockAltar;
	public static BlockDarkDome blockDarkDome;

	// Rune blocks
	public static BlockRune blockRuneEmpty;
	public static BlockRune blockRuneBinding;
	public static BlockRune blockRuneOpening;
	public static BlockRune blockRuneHolding;
	public static BlockRune blockRuneInvitation;

	public static void init()
	{
		// Init Blocks
		blockDarkness = new BlockDarkness();
		blockAltar = new BlockAltar();
		blockDarkDome = new BlockDarkDome();

		// Init runes
		blockRuneEmpty = new BlockRune(RuneType.EMPTY);
		blockRuneBinding = new BlockRune(RuneType.BINDING);
		blockRuneOpening = new BlockRune(RuneType.OPENING);
		blockRuneHolding = new BlockRune(RuneType.HOLDING);
		blockRuneInvitation = new BlockRune(RuneType.INVITATION);

		// Set blocks' tabs
		blockDarkness.setCreativeTab(DarkPowers.creativeTabDarkness);
		blockAltar.setCreativeTab(DarkPowers.creativeTabDarkness);

		// Set rune blocks' tabs
		blockRuneEmpty.setCreativeTab(DarkPowers.creativeTabDarkness);
		blockRuneBinding.setCreativeTab(DarkPowers.creativeTabDarkness);
		blockRuneOpening.setCreativeTab(DarkPowers.creativeTabDarkness);
		blockRuneHolding.setCreativeTab(DarkPowers.creativeTabDarkness);
		blockRuneInvitation.setCreativeTab(DarkPowers.creativeTabDarkness);

		// Register blocks
		GameRegistry.registerBlock(blockDarkness, "darkness_block");
		GameRegistry.registerBlock(blockAltar, "altar");
		GameRegistry.registerBlock(blockDarkDome, "dark_dome");

		// Register rune blocks
		GameRegistry.registerBlock(blockRuneEmpty, "rune_empty");
		GameRegistry.registerBlock(blockRuneBinding, "rune_binding");
		GameRegistry.registerBlock(blockRuneOpening, "rune_opening");
		GameRegistry.registerBlock(blockRuneHolding, "rune_holding");
		GameRegistry.registerBlock(blockRuneInvitation, "rune_invitation");
	}
}
